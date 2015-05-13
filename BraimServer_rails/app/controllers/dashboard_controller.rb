class DashboardController < ApplicationController
	before_action :authenticate_user!
  def index
  	@braim_sessions = current_user.braim_sessions
  end
end
