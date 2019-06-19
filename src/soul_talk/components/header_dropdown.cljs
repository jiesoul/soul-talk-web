(ns soul-talk.components.header-dropdown)

(defn header-dropdown [menu title]
  [:> js/antd.Dropdown {:overlay menu}
   [:a {:className "ant-dropdown-link"
        :href "#"}
    [:> js/antd.Icon {:type "user"}]
    " " title]
   ])
