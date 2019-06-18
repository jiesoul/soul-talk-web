(ns soul-talk.app
  (:require [soul-talk.core :as core]))

(set! *warn-on-infer* true)

(set! *print-fn* (fn [& _]))
(goog-define DEBUG false)
(core/init!)
