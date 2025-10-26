;; Capture all annotations
(annotation) @annotation

;; Capture class names
(class_declaration
  name: (identifier) @class)

;; Capture method declarations
(method_declaration
  name: (identifier) @method)

;; Capture method calls
(method_invocation
  name: (identifier) @method.call)
