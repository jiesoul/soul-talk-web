(ns soul-talk.coeffects
  (:require [re-frame.core :as rf]
            [soul-talk.local-storage :as local-store]))

(rf/reg-cofx
  :now
  (fn [cofx _]
    (assoc cofx :now (js/Date.))))

(rf/reg-cofx
  :local-store
  (fn [cofx local-store-key]
    (assoc cofx :local-store (local-store/get-item local-store-key))))
