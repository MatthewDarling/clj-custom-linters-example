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

# Writing a custom linter

An Eastwood linter accepts two arguments:

  * `analyze-results`: The return value of
     [Eastwood's `analyze-ns`](https://github.com/jonase/eastwood/blob/master/src/eastwood/analyze_ns.clj#L320).
     It's an augmented form of
     `clojure.tools.analyzer/analyze+eval`. The
     [tools.analyzer quickref](http://clojure.github.io/tools.analyzer/spec/quickref.html) is
     very helpful to document where you will find the data
     you're looking for. To play with the analysis of a namespace
     you want to lint,
     use
     [`eastwood.lint/insp`](https://github.com/jonase/eastwood/blob/master/src/eastwood/lint.clj#L1316). Note
     that the returned data structure is very, very large so you
     probably don't want to print it to your REPL.
  * `opts`: The Eastwood options map. It gets processed a bit beyond
    what was directly in your `project.clj` or `profiles.clj`.

An Eastwood linter returns a sequence of
 [lint warning maps](https://github.com/MatthewDarling/clj-custom-linters-example/blob/master/linters/doc_lints.clj#L31-L34).

Your custom linters can be arbitrarily complex, with only two
significant restrictions:

  1. You can't use any dependencies that Eastwood itself doesn't use.
  2. You have to analyze one namespace at a time.

Check out
the
[linter that checks public functions for docstrings](https://github.com/MatthewDarling/clj-custom-linters-example/blob/master/linters/doc_lints.clj) for
a simple example, and the [linter for public functions to have
`clojure.spec` definitions](https://github.com/MatthewDarling/clj-custom-linters-example/blob/master/linters/spec_lints.clj) for
a more complex example.

## License

Copyright © 2017 Matthew Darling

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
