FactoryGirl.define do
  factory :song do
    name {"Test Song #{Song.count + 1}"}
    duration {300000}
    file Rack::Test::UploadedFile.new(File.open(File.join(Rails.root, '/spec/factories/files/OneRepublic - Counting Stars.mp3')))
  end
end
