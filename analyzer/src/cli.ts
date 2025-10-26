#!/usr/bin/env node
import fs from "fs";
import path from "path";
import { analyzeJavaFile } from "./extract-java";

const folder = process.argv[2];
if (!folder) {
  console.error("Usage: ts-node src/cli.ts <source-folder>");
  process.exit(1);
}

// Recursively scan for Java files
const javaFiles: string[] = [];
function scan(dir: string) {
  const items = fs.readdirSync(dir);
  for (const item of items) {
    const full = path.join(dir, item);
    const stat = fs.statSync(full);
    if (stat.isDirectory()) scan(full);
    else if (full.endsWith(".java")) javaFiles.push(full);
  }
}
scan(folder);

// Analyze files
const results = javaFiles.map(f => analyzeJavaFile(f));
console.log(JSON.stringify(results, null, 2));
