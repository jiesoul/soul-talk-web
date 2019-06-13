(ns soul-talk.components.header-dropdown)

(defn header-dropdown [menu title]
  [:> js/Dropdown {:overlay menu}
   [:a {:className "ant-dropdown-link"
        :style {:color "#FFF"}
        :href "#"}
    [:> js/Icon {:type "user"}]
    " " title]
   ])
