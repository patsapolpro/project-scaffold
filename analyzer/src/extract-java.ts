import fs from "fs";
import { Query } from "tree-sitter";
import Java from "tree-sitter-java";
import { parseJavaCode } from "./parser";

export function analyzeJavaFile(file: string) {
  const root = parseJavaCode(file);
  const query = new Query(Java, fs.readFileSync("queries/java/spring.scm", "utf8"));
  const matches = query.matches(root);

  const result: any = {
    file,
    class: "",
    type: "unknown",        // controller/service/repository
    endpoints: [],
    methods: [],
    calls: [],
    annotations: []
  };

  for (const { captures } of matches) {
    for (const { name, node } of captures) {
      const text = node.text;
      switch (name) {
        case "class":
          result.class = text;
          break;
        case "annotation":
        case "markerAnnotation":
        case "normalAnnotation":
          result.annotations.push(text);

          // Identify layer
          if (/Controller/.test(text)) result.type = "controller";
          if (/Service/.test(text)) result.type = "service";
          if (/Repository/.test(text)) result.type = "repository";

          // Extract HTTP endpoint path if mapping
          if (text.includes("Mapping")) {
            const pathMatch = text.match(/"(.*?)"/);
            if (pathMatch) result.endpoints.push(pathMatch[1]);
          }
          break;
        case "method":
          result.methods.push(text);
          break;
        case "method.call":
          result.calls.push(text);
          break;
      }
    }
  }

  return result;
}
