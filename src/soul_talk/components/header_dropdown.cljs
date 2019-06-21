(ns soul-talk.components.header-dropdown)

(defn header-dropdown [menu title]
  [:> js/antd.Dropdown {:overlay menu
                        :style {:color "#000"}}
   [:a {:className "ant-dropdown-link"
        :href "#"}
    [:> js/antd.Icon {:type "user"}]
    "  " title]
   ])
