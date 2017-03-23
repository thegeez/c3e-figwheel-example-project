(ns hello-world.connect
  (:require [ajax.core :refer [GET POST]]))

(defonce timer (atom nil))

(defn handler [response app-state]
  (swap! app-state 
          (fn [s]
            (merge-with max s (select-keys response [:clj :cljs]))))
  (println "response: " response))

(defn again [f]
  (swap! timer
         (fn [old-timer]
           (when old-timer
             (.clearTimeout js/window old-timer))
           (.setTimeout js/window
                        f
                        1000))))

(defn vote [choice]
  (POST (str "/vote-" (name choice))))

(defn read-tally [app-state]
  (GET "/tally"
       {:response-format :json
        :keywords? true
        :handler (fn [response]
                   (handler response app-state)
                   (again (fn []
                            (read-tally app-state))
                          ))
        :error-handler (fn [{:keys [status status-text]}]
                         (println "error-handler:" status status-text)
                         (again (fn []
                            	  (read-tally app-state))))}))