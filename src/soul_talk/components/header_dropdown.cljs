(ns soul-talk.components.header-dropdown
  (:require [antd :as antd]))

(defn header-dropdown [menu title]
  [:> antd/Dropdown {:overlay menu}
   [:a {:className "ant-dropdown-link"
        :style {:color "#FFF"}
        :href "#"}
    [:> antd/Icon {:type "user"}]
    " " title]
   ])
