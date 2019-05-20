(ns soul-talk.pages.landing
  (:require [antizer.reagent :as ant]
            [reagent.core :as r]))

(defn header-component []
      [:> js/antd.Layout
       [:> js/antd.Input {:value       "soul"
                       :id          "test-id"
                       :placeholder "你的测试"}]])

(defn base-layout []
  (fn []
    [ant/locale-provider {:locale (ant/locales "zh_CN")}
     [ant/layout
      [ant/affix
       [ant/layout-header {:class "banner light small"}
        (r/as-element
          [ant/row
           [ant/col {:span 12} [:h2.banner-header "Soul Talk"]]
           [ant/col {:span 1 :offset 11}
            [:a {:href ""}
             [ant/icon {:class "banner-logo" :type "github"}]]]])
        ]]]]))