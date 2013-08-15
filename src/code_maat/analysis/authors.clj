(ns code-maat.analysis.authors
  (:require [code-maat.dataset.dataset :as ds]
            [code-maat.analysis.entities :as entities]))

;;; This module contains analysis methods related to the authors of the VCS commits.
;;; Research shows that these metrics (e.g. number of authors of a module) are
;;; related to the number of quality problems that module exhibits.
;;;
;;; Format:
;;; All analysis expect an Incanter dataset with the following columns:
;;; :author :entity :rev

(defn of-module [m ds]
  (set
   (ds/-select-by
    :author
    (ds/-where {:entity m} ds))))

(defn all
  "Returns a set with the name of all authors."
  [ds]
  (set (ds/-select-by :author ds)))

(defn entity-with-author-count
  "Calculates the number of different authors for the given module, m.
   Returns a tuple of [entity-name number-of-distinct-authors]."
  [m ds]
  [m (count (of-module m ds))])

(defn- group->entity-with-author-count
  [[entity-group changes] ds]
  (let [entity (:entity entity-group)]
    [entity
     (count
      (set
       (ds/-select-by :author changes)))
     (entities/revisions-of entity ds)]))

(defn by-count
  "Groups all entities by there total number of authors.
   By default, the entities are sorted in descending order.
   You can provide an extra, optional argument specifying
   a custom criterion.
   Returns a dataset with the columns :entity :n-authors."
  ([ds]
     (by-count ds :desc))
  ([ds order-fn]
     (let [g (ds/-group-by :entity ds)
           by-rev (entities/as-dataset-by-revision ds)]
       (ds/-order-by :n-authors order-fn
                     (ds/-dataset [:entity :n-authors :n-revs]
                                  (map
                                   #(group->entity-with-author-count % by-rev) g))))))