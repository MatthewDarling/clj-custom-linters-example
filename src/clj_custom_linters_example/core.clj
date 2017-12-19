(ns clj-custom-linters-example.core
  (:require [clojure.spec.alpha :as s]))

(s/fdef public-inc
        :args (s/cat :x number?)
        :ret number?)

(defn public-inc
  "A public function must have a spec and a docstring. This one has
  both, so it won't trigger any warnings from our custom linters."
  [x]
  (inc x))

(defn public-mult
  "This function has no spec, so it will trigger a warning from our
  custom linter."
  [x y]
  (* x y))

(s/fdef undocumented-mess
        :args (s/cat :x number?)
        :ret number?)

;;; This function has no docstring, so it will trigger a warning from our custom linter
(defn undocumented-mess
  [x]
  (- (+ (- (/ (+ 0 (* 1 x)) 1) 0)
        (- (/ (+ 0 (* 1 x)) 1) 0))
     (- (/ (+ 0 (* 1 x)) 1) 0)))

(defn- private-inc
  "A private function doesn't need to have a spec or docstring."
  [x]
  (inc x))
