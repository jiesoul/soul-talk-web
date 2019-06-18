(ns soul-talk.layouts.user-layout
  (:require [soul-talk.components.global-footer :refer [footer]]
            [reagent.core :as r]
            ))

(defn copyright []
  [:> js/antd.Layout.Footer
   "Copyright "
   [:> js/antd.Icon {:type "copyright"}]
   " 2019 "])

(defn user-layout [children]
  [:> js/antd.Layout {:title ""}
   [:> js/antd.Layout.Content {:style {:min-height "100vh"
                                    :padding "24px 0 20px 0"}}
    children
    copyright]])
