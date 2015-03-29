module Songbook
  module Entities
    class Token < Grape::Entity
      expose :access_token, documentation: { type: "string", desc: "Real access token", example: "most_secure_password" }
      expose :expires_in, documentation: { type: "string", desc: "expiration time (1 day)", example: "2014-06-09T13:50:52-05:00" }
      expose :user_logged, documentation:  { type: "string", desc: "username of the user logged", example: "testuser" }
    end
  end
end
