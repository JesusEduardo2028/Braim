class AccessGrant
  include Mongoid::Document
  include Mongoid::Timestamps

  field :access_token, :type => String
  field :access_token_expires_at, :type => Time

  belongs_to :user

  before_create :gen_tokens
  after_create :start_expiry_period!

  # defines a class mehtod that allows find any matches regarding specific token
  # @param access_token [String], secured key associated with temporal token
  # @return [AccesGrant], access_grant instance with reference to current user
  def self.find_access(access_token)
    where(:access_token => access_token).
      any_of(
             {:access_token_expires_at => nil},
             {:access_token_expires_at.gt => Time.now}).first
  end

  # Removes all access grant references which their life span has finished
  def self.prune!
    where(:created_at.lte => 2.hours.ago).delete_all
  end

  # Update access_token_expires_at field
  def start_expiry_period!
    self.update_attribute(:access_token_expires_at, 2.hours.from_now) unless self.access_token_expires_at.present?
  end

  protected
  # generates tokens associated with code, access_token and refresh_token
  def gen_tokens
    self.access_token = SecureRandom.hex(16)
  end

end
