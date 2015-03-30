FactoryGirl.define do
  factory :emo_session do
    start_at {Time.now}
    user {FactoryGirl.build(:user)}
  end
end
