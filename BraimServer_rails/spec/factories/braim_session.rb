FactoryGirl.define do
  factory :braim_session do
    start_at {Time.now}
    user {FactoryGirl.build(:user)}
  end
end
