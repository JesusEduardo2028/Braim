class PlayerEntry
  include Mongoid::Document
  belongs_to :emo_session

  field :timestamp, type: Float
  field :song_id, type: BSON::ObjectId
  field :action , type: Symbol

  def player_state
    action == :play ? 1 : 0
  end
  def song
    Song.find(song_id)
  end
end
