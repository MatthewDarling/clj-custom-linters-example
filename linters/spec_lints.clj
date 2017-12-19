(ns spec-lints
  "`clojure.spec` related linters for Eastwood to run.

  A namespace definition is optional for lint files - they are
  evaluated in the `eastwood.util` namespace. However, it's much more
  robust to act like you're writing a full Clojure source file,
  instead of a list of forms to dump in another project's namespace."
  (:require [clojure.set :as set]
            [eastwood.copieddeps.dep1.clojure.tools.analyzer.ast :as ast]
            [eastwood.util :as util]))

(defn private?
  "Check if `ast` represents a private var definition."
  [ast]
  (-> ast :meta :form :private))

(defn namespaced-symbol
  "Combine two symbols `the-namespace` and `the-name` into a
  namespaced symbol, eg `the-namespace/the-name`.

  `clojure.core/symbol` doesn't support combining symbols this way."
  [the-namespace the-name]
  (symbol (if (symbol? the-namespace)
            (name the-namespace)
            the-namespace)
          (if (symbol? the-name)
            (name the-name)
            the-name)))

(defn var-desc-if-defn-or-fdef
  "If `ast` represents either an invocation of `fdef` or `defn`,
  return a map describing the var. Otherwise, return `nil`."
  [analyzed-ns ast]
  (cond
    (= "clojure.spec.alpha/def-impl" (-> ast :fn :form str))
    {:type :spec-def
     :target-sym (-> ast :args first :expr :form)
     :ast ast}

    (and (util/get-fn-in-def ast)
         (not (private? ast)))
    {:type :defn
     :target-sym (namespaced-symbol analyzed-ns (:name ast))
     :ast ast}

    :else nil))

(defn public-fn-has-spec
  [analyze-results opts]
  (let [relevant-exprs (->> analyze-results
                            :asts
                            (mapcat ast/nodes)
                            (keep #(var-desc-if-defn-or-fdef (:namespace analyze-results) %)))
        related-exprs-map (group-by :target-sym relevant-exprs)]
    (for [[target-sym related-exprs] related-exprs-map
          :when (not (set/subset? #{:defn :spec-def}
                                  (set (map :type related-exprs))))]
      (let [defn-expr (first (filter #(= :defn (:type %)) related-exprs))
            ^clojure.lang.Var v (:var (:ast defn-expr))
            s (.sym v)
            loc (:env defn-expr)]
        (util/add-loc-info loc
                           {:linter :spec-for-every-public-fn
                            :msg (format "%s needs a spec" v)})))))

(util/add-linter
 {:name :spec-for-every-public-fn
  :fn public-fn-has-spec})
