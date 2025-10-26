import Parser from "tree-sitter";
import Java from "tree-sitter-java";
import fs from "fs";

export function parseJavaCode(file: string) {
  const parser = new Parser();
  parser.setLanguage(Java);
  const code = fs.readFileSync(file, "utf8");
  return parser.parse(code).rootNode;
}
