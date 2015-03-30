class User
  include Mongoid::Document
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  attr_accessor :login
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable

  ## Database authenticatable
  field :email,              type: String, default: ""
  field :encrypted_password, type: String, default: ""
  field :username,           type: String, default: ""
  ## Recoverable
  field :reset_password_token,   type: String
  field :reset_password_sent_at, type: Time

  ## Rememberable
  field :remember_created_at, type: Time

  ## Trackable
  field :sign_in_count,      type: Integer, default: 0
  field :current_sign_in_at, type: Time
  field :last_sign_in_at,    type: Time
  field :current_sign_in_ip, type: String
  field :last_sign_in_ip,    type: String

  has_one :access_grant
  has_many :emo_sessions

  ## Confirmable
  # field :confirmation_token,   type: String
  # field :confirmed_at,         type: Time
  # field :confirmation_sent_at, type: Time
  # field :unconfirmed_email,    type: String # Only if using reconfirmable

  ## Lockable
  # field :failed_attempts, type: Integer, default: 0 # Only if lock strategy is :failed_attempts
  # field :unlock_token,    type: String # Only if unlock strategy is :email or :both
  # field :locked_at,       type: Time

  # validates user credentials
  # @return User
  validates :username, format: { with: /\A[a-zA-Z0-9]+\Z/ }, uniqueness: true
  def self.authenticate(data, password)
    begin
      return User.find_by(username: data) if User.find_by(username: data).try(:valid_password?, password)
    rescue
      return User.find_by(email: data) if User.find_by(email: data).try(:valid_password?, password)     
    end
  end

  def self.serialize_from_session(key, salt)
    record = to_adapter.get(key[0]["$oid"])
    record if record && record.authenticatable_salt == salt
  end

  def self.serialize_from_cookie(id, remember_token)
    record = to_adapter.get(id[0]["$oid"])
    record if record && !record.remember_expired? &&
    Devise.secure_compare(record.rememberable_value, remember_token)
  end

  def all_emo_entries
    return EmoEntry.where(user_id: /#{id.to_s}/)
  end

  def all_player_entries
    return PlayerEntry.where(user_id: /#{id.to_s}/)
  end

  def self.find_first_by_auth_conditions(warden_conditions)
    conditions = warden_conditions.dup
    if login = conditions.delete(:login)
      self.any_of({ :username =>  /^#{Regexp.escape(login)}$/i }, { :email =>  /^#{Regexp.escape(login)}$/i }).first
    else
      super
    end
  end
end
