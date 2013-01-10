(ns jukebox-web.models.artwork-test
  (:require [jukebox-web.models.artwork :as artwork])
  (:use [clojure.test]
        [clojure.contrib.http.agent :only (success?)]
        [clojure.contrib.mock]))

(deftest shows-album-cover
  (testing "returns default artwork if album isnt found"
    (let [album (artwork/album-cover "user" "test")]
      (is (not (nil? album)))
      (is (= {:large "/img/no_art_lrg.png" :extra-large "/img/no_art_lrg.png"}
             album))))

  (testing "returns default artwork if the HTTP request fails"
    (expect [success? (calls (fn [http-agent] (throw (Exception. "failed"))))]
      (let [album (artwork/album-cover "user" "test")]
        (is (not (nil? album)))
        (is (= {:large "/img/no_art_lrg.png" :extra-large "/img/no_art_lrg.png"}
               album)))))

  (testing "finds artwork for an album"
    (let [album (artwork/album-cover "21" "Adele")]
      (is (not (nil? album)))
      (is (= {:large "http://userserve-ak.last.fm/serve/174s/55125087.png"
              :extra-large "http://userserve-ak.last.fm/serve/_/55125087/21++600x600+HQ+PNG.png"}
             album)))))
