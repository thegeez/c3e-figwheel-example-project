(ns hello-world.server)

(defonce votes (atom {:clj 0
                      :cljs 0}))

(defn ring-handler [req]
    (cond 
      (= (:uri req) "/tally")
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (let [{:keys [clj cljs]} @votes]
               (str "{\"clj\": " clj ", \"cljs\": " cljs "}"))}
      (= (:uri req) "/vote-clj")
      (do
        (swap! votes update :clj inc)
        {:status 201})
      (= (:uri req) "/vote-cljs")
      (do
        (swap! votes update :cljs inc)
        {:status 201})
      :else
      {:status 500
       :body (str "Server error" req)}))

(defn handler [req]
  ;; reload code for every request
  (require 'hello-world.server :reload)
  (ring-handler req))