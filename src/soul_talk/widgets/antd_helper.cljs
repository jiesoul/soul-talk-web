(ns soul-talk.widgets.antd-helper
  )

(defn decorate-field
  ([form id field] (decorate-field form id {} field))
  ([form id options field]
   (let [field-decorator (:getFieldDecorator form)])))
