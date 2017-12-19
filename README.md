# clj-custom-linters-example

An example repo showing how to use a
[proposed new Eastwood feature allowing for custom linters](https://github.com/jonase/eastwood/pull/239).
 To try it yourself, check out that branch of Eastwood and run `lein install`.

Then run `lein eastwood` on this project. Your output will look like
this:

```bash
$ lein eastwood
== Eastwood 0.2.6-beta2 Clojure 1.9.0 JVM 1.8.0_131
Directories scanned for source files:
  src
== Linting clj-custom-linters-example.core ==
Entering directory `/Users/matthewdarling/code/clj-custom-linters-example'
src/clj_custom_linters_example/core.clj:25:1: docstring-for-every-public-fn: #'clj-custom-linters-example.core/undocumented-mess needs a docstring
src/clj_custom_linters_example/core.clj:1:1: spec-for-every-public-fn: #'clj-custom-linters-example.core/public-mult needs a spec
== Warnings: 2 (not including reflection warnings)  Exceptions thrown: 0
```

The files listed in the
 [`:lint-files` argument](https://github.com/MatthewDarling/clj-custom-linters-example/blob/master/project.clj#L9)
 of the `:eastwood` configuration are evaluated by Eastwood. If they
contain calls to `eastwood.util/add-linter`, those linters will be
included when you run Eastwood, and reported alongside any built-in
lint warnings.

## License

Copyright © 2017 Matthew Darling

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
