(ns soul-talk.components.global-footer
  )



(defn footer []
  [:> js/antd.Layout.Footer {:style {:text-align "center"
                                    :background "#3e3e3e"}}
   [:> js/antd.Row
    [:h4 {:style {:color "#FFF"}}
     "Made with By "
     [:a
      {:type   "link"
       :href   "https://ant.design"
       :target "_blank"}
      "Ant Design"]
     " and JIESOUL "]]])
