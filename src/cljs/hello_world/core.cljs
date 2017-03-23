(ns hello-world.core
  (:require [reagent.core :as reagent]
            [hello-world.connect :as connect]))

(enable-console-print!)

(println "This text is printed from src/hello-world/core.cljs. Go ahead and edit it and see reloading in action!")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (reagent/atom {:clj 0, :cljs 0}))

(defn bars [state]
  (let [{:keys [clj cljs]} state
        clj-height (max (* (/ clj (+ clj cljs)) 100) 0)
        cljs-height (max (* (/ cljs (+ clj cljs)) 100) 0)]
    [:div {:style {:height "400px"
                   :width "200px"}}
   	  [:div {:style {:float "left"
                     :backgroundColor "green"
                     :height (str clj-height "%")}}
        "CLJ"]
      [:div {:style {:float "right"
                     :backgroundColor "blue"
                     :height (str cljs-height "%")}}
        "CLJS"]]))

(defn hello-world []
  (reagent/with-let 
    [add-vote (fn [choice]
                (fn [e] (println "add vote" choice)
                  (swap! app-state update choice (fnil inc 0))
                  (connect/vote choice)))]
    [:div
     [:h1 "http://vote.thegeez.net"]
   	 [:h1 (:text @app-state)
    		"which one do you like?"]
     [:h2 {} @app-state]
   	 [bars @app-state]
     [:button {:on-click (add-vote :clj)}
      "Vote Clojure"]
     [:button {:on-click (add-vote :cljs)}
      "Vote ClojureScript"]]))

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(connect/read-tally app-state)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  (swap! app-state update-in [:__figwheel_counter] inc)
)
