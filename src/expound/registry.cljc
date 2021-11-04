(ns expound.registry
  (:require [clojure.spec.alpha :as s]
            [expound.util :as u]))

;;;;;; registry ;;;;;;

(def ^:private registry-ref (atom {::error-messages {}
                                   ::spec-names {}}))

(defn spec-name
  [spec]
  (get-in @registry-ref [::spec-names spec]))

(defn error-message
  [k]
  (get-in @registry-ref [::error-messages k]))

(defn assoc-spec-name
  [k display-name]
  (swap! registry-ref
         assoc-in
         [::spec-names k]
         display-name))

(defn assoc-error-message [k error-message]
  (swap! registry-ref
         assoc-in
         [::error-messages k]
         error-message))

(defn find-registry-name
  "Will try recursively to find a name for a spec on the registry"
  [spec-ident]
  (u/visit-spec-parents spec-ident spec-name))


;; (prn @registry-ref)
