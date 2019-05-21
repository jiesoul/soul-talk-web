(ns soul-talk.app
  (:require [soul-talk.core :as core]
            [react]
            [antd]
            [button]
            [devtools.core :as devtools]))

(def api-url "http://localhost:3001")

(enable-console-print!)

(js/console.log react)
(js/console.log button)
(js/console.log js/button)

(prn "hello world!")

(devtools/set-pref! :dont-detect-custom-formatters true)
(devtools/install!)

;(core/init!)