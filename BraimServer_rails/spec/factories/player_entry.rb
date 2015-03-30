FactoryGirl.define do
  factory :player_entry do
    emo_session {FactoryGirl.build(:emo_session)}
    user_id {"user_id"}
    timestamp {Time.now}
    song_id {"song_id"}
    action {"play"}
  end
end
