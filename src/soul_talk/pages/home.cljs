(ns soul-talk.pages.home
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [soul-talk.pages.common :as c]
            [ant.design]
            [antd])
  (:import [goog.history.Html5History]))

(defn blog-header-component []
  (fn []
    [:> antd/Row
     [:div.row.flex-nowrap.justify-content-between.align-items-center
      [:> antd/Col {:span 4}
       [:a.text-muted {:href "#"} "Subscribe"]]
      [:> antd/Col {:span 16}
       [:a.blog-header-logo.text-dark {:href "/"} "Soul Talk"]]
      [:> antd/Col {:span 4}
       [:a.text-muted {:href "#"} "About"]]]]))

(defn nav-scroller-header-component []
  (r/with-let [navs (subscribe [:categories])]
    (fn []
      [:div.nav-scroller.py-1.mb-2
       [:nav.nav.d-flex.justify-content-between
        (for [{:keys [id name] :as nav} @navs]
          ^{:key nav}
          [:a.p-2.text-muted {:href (str id)} name])]])))

(defn jumbotron-header-component []
  (fn []
    [:> antd/Layout
     [:> antd/Carousel
      [:div.col-md-6.px-0.text-center
       [:h2.display-4.font-italic "进一步有一步的欢喜"]]]]))

(defn menu-component []
  (fn []
    [:> antd/Menu {:theme "dark"
                   :mode "horizontal"
                   :default-select-keys ["2"]
                   :style {:line-height "64px"}}
     [:> antd/Menu.Item {:key "1"} "blog"]
     [:> antd/Menu.Item {:key "2"} "timeline"]
     [:> antd/Menu.Item {:key "3"} "about"]
     ]))

(defn header-component []
  (fn []
    [:> antd/Layout
     [:div {:className "logo"}]
     [:> antd/Row
      [menu-component]]]))

(defn footer-component []
  (fn []
    [:div.container.blog-footer
     [:p
      [:a.text-muted {:href "#"} "Back to top"]]]))

(defn blog-post-component []
  (r/with-let [posts (subscribe [:posts])
               pagination (subscribe [:pagination])
               offset (r/cursor pagination [:offset])
               page (r/cursor pagination [:page])
               prev-page (r/cursor pagination [:previous])
               next-page (r/cursor pagination [:next])
               pre-page (r/cursor pagination[:pre-page])
               total-pages (r/cursor pagination [:total-pages])]

    (fn []
      [:> antd/Row
       [:h3.pb-3.mb-4.font-italic.border-bottom
        "文章"]
       (for [{:keys [id title create_time author content] :as post} @posts]
         ^{:key post} [:div.blog-post
                       [:h2.blog-post-title
                        [:a.text-muted
                         {:href   (str "/posts/" id)
                          :target "_blank"}
                         title]]
                       [:p.blog-post-meta (str (.toDateString (js/Date. create_time)) " by " author)]
                       [:hr]
                       [:div [c/markdown-preview content]]])
       [:nav.blog-pagination
        [:a.btn.btn-outline-primary
         {:on-click #(dispatch [:load-posts {:page     @next-page
                                             :pre-page @pre-page}])
          :class    (if (>= @page @total-pages) "disabled")}
         "Older"]
        [:a.btn.btn-outline-secondary
         {:on-click #(dispatch [:load-posts {:page     @prev-page
                                             :pre-page @pre-page}])
          :class    (if (zero? @offset) "disabled")}
         "Newer"]]])))

(defn where-component []
  (fn []
    [:div.p-3
     [:h4.font-italic "联系我"]
     [:ol.list-unsty
      [:li [:a {:href "https://github.com/jiesoul"
                :target "_blank"}
            [:i.fa.fa-github]
            " GitHub"]]
      [:li [:a {:href "https://weibo.com/jiesoul"
                :target "_blank"}
            [:i.fa.fa-weibo] " Weibo"]]
      [:li [:a {:href "https://twitter.com/jiesoul1982"
                :target "_blank"}
            [:i.fa.fa-twitter]
            " Twitter"]]]]))

(defn archives-component []
  (r/with-let [posts-archives (subscribe [:posts-archives])]
    (fn []
      [:div.p-3
       [:h4.font-italic "Archives"]
       [:ol.list-unstyled.mb-0
        (for [{:keys [year month] :as archive} @posts-archives]
          ^{:key archive}
          [:li [:a {:href (str "/posts/archives/" year "/" month)} (str month " " year)]])]])))

(defn main-component []
  (fn []
    [:> antd/Layout {:class "layout"}
     [:> antd/Layout.Content {:class "layout"}
      [jumbotron-header-component]]
     [:> antd/Row
      [:> antd/Col {:span 16}
       [blog-post-component]]
      [:> antd/Col {:span 6}
       [:aside.col-md-4.blog-sidebar
        [:div.p-3.mb-3.bg-light.rounded
         [:h4.font-italic "About"]
         [:p.mb-0 ""]
         [where-component]]
        [archives-component]]]]]))

(defn integration []
  [:div
   [:div.foo "hello " [:strong "world"]]
   (r/create-element "div"
                     #js{:className "foo"}
                     "hello "
                     (r/create-element "strong"
                                       #js{}
                                       "world"))
   (r/create-element "div"
                     #js{:className "foo"}
                     "hello "
                     (r/create-element [:strong "word"]))
   [:div.foo "hello " (r/create-element "strong"
                                        #js{}
                                        "world")]])

(defn home-component []
  (fn []
    [:div
     [:> antd/Layout {:class "layout"}
      [:> antd/Layout.Header {:class "header"}
       [header-component]]
      [:> antd/Layout.Content {:style {:padding "24px"}}
       [main-component]]
      [:> antd/Layout.Footer
       [footer-component]]]]))

(defn home-page []
  [home-component])