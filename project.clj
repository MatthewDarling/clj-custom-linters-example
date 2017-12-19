(defproject clj-custom-linters-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :plugins [[jonase/eastwood "0.2.6-beta2"]]
  :eastwood {:namespaces [:source-paths]
             :lint-files ["linters/spec_lints.clj" "linters/doc_lints.clj"]})
