(ns soul-talk.dev
  (:require [soul-talk.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(prn "hello world!")

(devtools/set-pref! :dont-detect-custom-formatters true)
(devtools/install!)

(core/init!)