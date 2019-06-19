(ns soul-talk.pages.home
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.layouts.home-layout :refer [layout]]
            [soul-talk.components.post :refer [home-posts]]
            [soul-talk.pages.users :refer [where-component]]
            ))

(defn home-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    (fn []
      [layout
       [:div.home-wrapper-page1
        [home-posts]]])))