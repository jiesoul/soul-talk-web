(ns soul-talk.local-storage)

(defn set-item!
  [key val]
  (->> val
    (clj->js)
    (js/JSON.stringify)
    (.setItem (.-localStorage js/window) key)))

(defn get-item
  [key]
  (js->clj
    (->> key
      (.getItem (.-localStorage js/window))
      (.parse js/JSON))
    :keywordize-keys true))

(defn remove-item!
  [key]
  (.removeItem (.-localStorage js/window) key))
