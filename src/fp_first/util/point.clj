(ns fp-first.util.point)
  (defrecord Point [p coord])

(def Ra 5)
(def Rb (* Ra 1.5))

(defn dimens-euq [v1 v2]
  (reduce + (map #(Math/pow (- %1 %2) 2) v1 v2))
  )

(defn potential [point v]
  (reduce + (map #(Math/pow 2.17 (* (/ -4 (* Ra Ra)) (dimens-euq point %1))) v))
  )

(defn rem-potential [p p2 x x2]
  (- p2 (* p (Math/pow 2.17 (* (/ -4 (* Rb Rb)) (dimens-euq x x2)))))
  )