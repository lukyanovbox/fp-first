(ns fp-first.core
  (:gen-class))
(require 'fp-first.util.parse-file )
(require 'fp-first.util.point )

(def e-up 0.5)
(def e-down 0.15)
(def points (into (sorted-map)  (map-indexed #(assoc {} %1 %2) (persistent!
                                                                 (fp-first.util.parse-file/parse-fl
                                                                   "resources\\irises.txt")))))
(def potentials (into (sorted-map)
                      (map-indexed #(assoc {} %2 %1)
                                   (map #(fp-first.util.point/potential %1 (vals points))
                                        (vals points)))))
(def p1 (apply max (keys potentials)))

(defn- dmin [cur-centr cntrs]
  (apply min (map #(fp-first.util.point/dimens-euq (get points (val cur-centr)) %1)  (map #(get points %1) (vals cntrs))) )
)

(defn- re-count [cur-centr rmng-ptntls points]
  (into (sorted-map) (map  #(assoc {} %1 %2) (map #(fp-first.util.point/rem-potential (key cur-centr)
                                                                                      %1
                                                                                      (get points (val cur-centr))
                                                                                      (get points (get rmng-ptntls %1)))
                                                  (keys rmng-ptntls))
                           (vals rmng-ptntls)))

  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (loop [rmng-ptntls     (fp-first.core/re-count (find potentials (apply max (keys potentials))) (dissoc potentials p1) points)
         cur-centr       (find rmng-ptntls (apply max (keys rmng-ptntls)))
         cntrs           (merge {} (find potentials (apply max (keys potentials))))
         ]


      (if (or (>= (key cur-centr) (* e-up p1) )
              (and (> (key cur-centr) (* e-down p1))
                   (>= (+ (/ (dmin cur-centr cntrs) fp-first.util.point/Ra)))))
        (recur (fp-first.core/re-count cur-centr (dissoc rmng-ptntls (key cur-centr)) points)
               (find rmng-ptntls (apply max (keys rmng-ptntls)))
               (merge cntrs cur-centr))

        (if (and (> (key cur-centr) (* e-down p1))
                 (< (+ (/ (dmin cur-centr cntrs) fp-first.util.point/Ra))))
           (let [rmn-potnls (assoc (dissoc rmng-ptntls (key cur-centr)) 0 (val cur-centr))]
             (recur rmn-potnls
                    (find rmn-potnls (apply max (keys rmn-potnls)))
                    cntrs
             ))
           (map #(find points %1) (vals cntrs))
        )
      )
  )
  )


