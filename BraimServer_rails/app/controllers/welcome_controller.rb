class WelcomeController < ApplicationController
  def index
  end

  def about
  end

  def setup
  end

  def ping
    respond_to do |format|
      format.html { render :text => 'pong!' }
    end
  end

end
