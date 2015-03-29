class EmoSession
  include Mongoid::Document

  has_many :emo_entries
  has_many :player_entries
  belongs_to :user

  field :start_at , type: Float

  before_create :set_time

  def date
    Time.at(start_at/1000).to_formatted_s(:long)
  end
  
  protected

  def set_time
    self.start_at = Time.now
  end
end
