(ns soul-talk.test-runner
  (:require [clojure.test :refer-macros [run-tests]]
            [cljs-test-display.core]))

(run-tests (cljs-test-display.core/init! "app-testing"
             ))
