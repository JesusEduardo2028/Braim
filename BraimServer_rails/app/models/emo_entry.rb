class EmoEntry
  include Mongoid::Document
  belongs_to :emo_session

  field :user_id, type: BSON::ObjectId
  field :nodes , type: Array
  field :timestamp, type: Float
  field :excitement, type: String
  field :frustration, type: String
  field :meditation, type: String
  field :engagement, type: String

end
