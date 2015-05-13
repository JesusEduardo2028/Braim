class BraimSession
  include Mongoid::Document
  before_create :set_time

  has_many :epoc_entries
  has_many :player_entries
  belongs_to :user

#------------------ MODEL ATTRIBUTES ---------------------#

  field :start_at , type: Float

#------------------ INSTANCE METHODS ---------------------#

  def date
    Time.at(start_at).to_formatted_s(:long)
  end
  
  protected

  def set_time
    self.start_at = Time.now
  end

end
