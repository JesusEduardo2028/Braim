# Description: A Epoc Entry is composed by a collection of collected data from the Emotiv EPOC device,
# including affectiv and raw data 
class EpocEntry
  include Mongoid::Document

  #--------------------- Associations ------------------------------#
  belongs_to :braim_session
  #--------------------- Fields ------------------------------#
  field :user_id, type: BSON::ObjectId
  field :nodes , type: Array
  field :timestamp, type: Float
  field :excitement, type: String
  field :frustration, type: String
  field :meditation, type: String
  field :engagement, type: String

end
