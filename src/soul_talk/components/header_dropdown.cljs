(ns soul-talk.components.header-dropdown)

(defn header-dropdown [menu title]
  [:> js/antd.Dropdown {:overlay menu}
   [:a {:className "ant-dropdown-link"
        :style {:color "#FFF"}
        :href "#"}
    [:> js/antd.Icon {:type "user"}]
    " " title]
   ])
