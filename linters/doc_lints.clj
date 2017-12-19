(ns doc-lints
  "Documentation related linters for Eastwood to run.

  A namespace definition is optional for lint files - they are
  evaluated in the `eastwood.util` namespace. However, it's much more
  robust to act like you're writing a full Clojure source file,
  instead of a list of forms to dump in another project's namespace."
  (:require [eastwood.copieddeps.dep1.clojure.tools.analyzer.ast :as ast]
            [eastwood.util :as util]))

(defn private?
  "Check if `ast` represents a private var definition."
  [ast]
  (-> ast :meta :form :private))

(defn docstring
  "Return the docstring of `ast`, if it represents a var with a
  docstring."
  [ast]
  (-> ast :meta :form :doc))

(defn public-fn-has-docstring
  [analyze-results opts]
  (for [expr (mapcat ast/nodes (:asts analyze-results))
        :when (and (util/get-fn-in-def expr)
                   (not (private? expr))
                   (not (docstring expr)))]
    (let [^clojure.lang.Var v (:var expr)
          s (.sym v)
          loc (:env expr)]
      (util/add-loc-info
       loc
       {:linter :docstring-for-every-public-fn
        :msg (format "%s needs a docstring" v)}))))

(util/add-linter
 {:name :docstring-for-every-public-fn
  :fn public-fn-has-docstring})
