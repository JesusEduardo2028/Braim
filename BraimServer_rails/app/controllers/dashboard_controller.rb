class DashboardController < ApplicationController
	before_action :authenticate_user!
  def index
  	@emo_sessions = current_user.emo_sessions
  end
end
