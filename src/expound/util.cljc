(ns ^:no-doc expound.util
  (:require [clojure.spec.alpha :as s]))

(def assert-message "Internal Expound assertion failed. Please report this bug at https://github.com/bhb/expound/issues")

(defn nan? [x]
  #?(:clj (and (number? x) (Double/isNaN x))
     :cljs (and (number? x) (js/isNaN x))))

;; some utils for spec walking

(defn- accept-keyword [x]
  (when (qualified-keyword? x)
    x))

(defn- spec-form
  "Return the spec form or nil."
  [spec]
  (some-> spec s/get-spec s/form))

(defn- parent-spec
  "Look up for the parent spec using the spec hierarchy."
  [k]
  (or (accept-keyword (s/get-spec k))
      (accept-keyword (spec-form k))))

(defn visit-spec-parents
  "will call `f` on spec key. If it returns nil, it will recur on the parent spec and try again.
  It's useful to apply `f` on a spec or specs that spec is aliased
  to."
  [spec-ident f]
  (if-let [x (f spec-ident)]
    x
    (when-let [parent-spec (some-> (parent-spec spec-ident) accept-keyword)]
      (recur parent-spec f))))
