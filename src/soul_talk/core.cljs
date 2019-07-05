(ns soul-talk.core
  (:require [reagent.core :as r]
            [soul-talk.ajax :refer [load-interceptors!]]
            [soul-talk.routes :refer [hook-browser-navigation! logged-in? navigate!]]
            [soul-talk.views :refer [main-page]]
            [re-frame.core :refer [dispatch-sync dispatch]]
    ;;初始化处理器和订阅器
            soul-talk.routes
            soul-talk.coeffects
            soul-talk.effects
            soul-talk.handlers
            soul-talk.subs))

;; 挂载页面组件
(defn mount-component []
  (r/render [#'main-page]
            (js/document.getElementById "app")))

;; 初始化方法
(defn init! []
  (dispatch-sync [:initialize-db])
  (if (logged-in?) (dispatch [:run-login-events]))
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-component))