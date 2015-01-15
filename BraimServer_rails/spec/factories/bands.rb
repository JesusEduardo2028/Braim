FactoryGirl.define do
  factory :band do
    name {"Test Band #{Band.count + 1}"}
    genre {"Test Genre #{Band.count + 1}"}
    biography {"Short Biography #{Band.count + 1}"}
  end
end
