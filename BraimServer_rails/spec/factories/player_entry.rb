FactoryGirl.define do
  factory :player_entry do
    braim_session
    user_id {"user_id"}
    timestamp {Time.now}
    song_id {"song_id"}
    action {"play"}
  end
end
