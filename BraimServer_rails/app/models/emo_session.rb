class EmoSession
  include Mongoid::Document

  has_many :emo_entries
  has_many :player_entries
  belongs_to :user

  field :start_at , type: Float

  def date
    Time.at(start_at/1000).to_formatted_s(:long)
  end
end
