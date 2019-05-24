(ns ^:figwheel-hooks soul-talk.app
  (:require [soul-talk.core :as core]
            [devtools.core :as devtools]))

(def api-url "http://localhost:3001")

(enable-console-print!)

(prn "hello world!")

(devtools/set-pref! :dont-detect-custom-formatters true)
(devtools/install!)

(core/init!)

(defn ^:before-load my-before-reload-callback []
  (prn "Before reload!!"))

(defn ^:after-load my-after-reload-callback []
  (prn "After reload!!"))