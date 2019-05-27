(ns soul-talk.app
  (:require [soul-talk.core :as core]
            [devtools.core :as devtools]
            [re-frame.core :as rf]))

(enable-console-print!)

(devtools/set-pref! :dont-detect-custom-formatters true)
(devtools/install!)

(rf/clear-subscription-cache!)
(core/init!)

